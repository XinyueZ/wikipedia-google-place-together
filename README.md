Sample wikipedia-google-place-together
===

[![Build Status](https://travis-ci.org/XinyueZ/wikipedia-google-place-together.svg?branch=master)](https://travis-ci.org/XinyueZ/wikipedia-google-place-together)

> This is a temporary repository  that will no longer be maintained.

- Use Google Android Place API 
	- No chance to load more places with place-api of play-service.
- Use Wikipedia Geo-Search
	- As compensation to use Wikipedia Api.
	
# Don't forget map-key, this repo doesn't contain.

#Android Studio IDE setup 

For development, the latest version of Android Studio 3.2 is required. The latest version can be
downloaded from [here](https://developer.android.com/studio/preview/).

Sunflower uses [ktlint](https://ktlint.github.io/) to enforce Kotlin coding styles.
Here's how to configure it for use with Android Studio (instructions adapted
from the ktlint [README](https://github.com/shyiko/ktlint/blob/master/README.md)):

- Close Android Studio if it's open

- Download ktlint:

  `curl -sSLO https://github.com/shyiko/ktlint/releases/download/0.24.0/ktlint && chmod a+x ktlint`

- Inside the project root directory run:

  `ktlint --apply-to-idea-project --android`

- Remove ktlint if desired:

  `rm ktlint`

- Start Android Studio
