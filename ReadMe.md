#Image Catalog

This application is showing details of items stored.

- This is single page appliction.
- On-launch of app it makes a API get call to fetch most recent 10 items from server.
- On scroll down the app dynamically make call to append 10 more older items to the view.
- All the call are made using authorization.
- With each call app test ssl pinning and as it is unsuccfull, it make network call with hostcomparision to add
TLS check.
- App also handles cookie.
- On re launch of app it does not make new call if there are not no new items added in the server, the view shows data
from Cache.

#Project
- Followed MVP architecture.
- Tried make proper seperation of concern.
- Used okhttp/retrofit library for service calls.
- Used adapter and recycler view for for view.
