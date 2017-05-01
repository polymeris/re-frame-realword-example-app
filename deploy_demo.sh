set -e
lein clean
lein cljsbuild once min
git clone git@github.com:polymeris/re-frame-realword-example-app-demo.git demo
cd demo
cp -R ../resources/public/* .
git config user.email '<deploy@conduit>'
git commit --allow-empty -am 'Update'
git push
cd ..
rm -Rf demo