#!/bin/bash

cd "${BASH_SOURCE%/*}" || exit

npm install

PUBLIC_URL=file:///android_asset/www \
  npm run build

rm -rf ../android/app/src/main/assets/www
cp -r build ../android/app/src/main/assets/www
