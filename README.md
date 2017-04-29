# ![RealWorld Example App](resources/logo.png)

> ### Clojurescript / [re-frame](https://github.com/Day8/re-frame) codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld-example-apps) spec and API.


### [Demo](https://polymeris.github.io/re-frame-realword-example-app-demo/)&nbsp;&nbsp;&nbsp;&nbsp;[RealWorld](https://github.com/gothinkster/realworld)

[![CircleCI](https://circleci.com/gh/polymeris/re-frame-realword-example-app.svg?style=svg)](https://circleci.com/gh/polymeris/re-frame-realword-example-app)

This codebase was created to demonstrate a fully fledged fullstack application built with 
Clojurescript and re-frame including CRUD operations, authentication, routing, pagination, and more.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

### Run tests:

```
lein clean
lein doo phantom test once
```

The above command assumes that you have [phantomjs](https://www.npmjs.com/package/phantomjs) installed. However, please note that [doo](https://github.com/bensu/doo) can be configured to run cljs.test in many other JS environments (chrome, ie, safari, opera, slimer, node, rhino, or nashorn).

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
