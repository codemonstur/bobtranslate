
[![GitHub Release](https://img.shields.io/github/release/codemonstur/bobtranslate.svg)](https://github.com/codemonstur/bobtranslate/releases) 
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)

# Bob-translate

Utils for managing internationalization files in a bob-plugin

So far only checks on:
- Missing keys
- Missing values

Future feature:
- Automatically complete translations by correlation

Lots of keys contain the same value, for example 'Close' or 'Add' buttons. It should be possible to lookup a missing translation by finding the same phrase in the file

- Automatically translate missing text using Google translate

This is harder than it might seem. Google created a hard to use library that also requires account data. Alternatively I could try scraping it off the website. Although Google makes that hard.

