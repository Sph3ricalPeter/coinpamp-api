**[ work in progress ]**

---

**ABOUT**

---

coinpamp api is the backend for the coinpamp application, which allows its users to track changes in cryptocurrency market prices after major social media posts.

---

**HOW IT WORKS**

---

The main purpose of this api is to cache posts from social media such as Reddit or Twitter and provide them for the coinpamp frontend as well as authorized users.

The API makes automatic requests to 3rd parties on an interval and updates the database. To make things easier, only top posts of today/month/year are saved to reduce clutter and overall size of the db.

Currently, the focus is on getting the MVP working with Reddit and Coinstats, as it is much easier to use the Reddit API then Twitter, due to a long and tedious application process for the Twitter API key.

---

**THE STACK**

---

* Java 11
* Spring Boot
* JUnit 5
* Maven
* PostgreSQL
* OpenAPI
* Docker

---