(ns conduit.schema
  (:require [cljs.spec :as spec]))

(spec/def ::email string?)
(spec/def ::token string?)
(spec/def ::username string?)
(spec/def ::bio (spec/nilable string?))
(spec/def ::image (spec/nilable string?))
(spec/def ::following boolean?)

(spec/def ::user (spec/keys :req-un [::email ::token ::username]
                            :opt-un [::username ::bio ::image ::id]))

(spec/def ::profile (spec/keys :req-un [::email ::username ::bio ::image ::following]
                               :opt-un [::id ::token]))

(spec/def ::slug string?)
(spec/def ::title string?)
(spec/def ::description string?)
(spec/def ::body string?)
(spec/def ::createdAt #(instance? js/Date %))
(spec/def ::updatedAt #(instance? js/Date %))
(spec/def ::favorited boolean?)
(spec/def ::favoritesCount (spec/and integer? (comp not neg?)))
(spec/def ::author ::profile)

(spec/def ::article (spec/keys :req-un [::slug ::title ::description
                                        ::body
                                        ::createdAt ::updatedAt
                                        ::favorited ::favoritesCount
                                        ::author]))
(spec/def ::articles (spec/coll-of ::article))

(spec/def ::comment (spec/keys :req-un [::id ::createdAt ::updatedAt ::body ::author]))
(spec/def ::coments (spec/coll-of ::coment))

(spec/def ::tag string?)
(spec/def ::tags (spec/coll-of ::tag))

(spec/def ::active-page keyword?)