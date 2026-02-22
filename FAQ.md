# FAQ

## Use cases

**How do I subscribe to topics?**
After logging in, go to the Topics page. Click "S'abonner" on any topic you're interested in. Posts from that topic will then appear in your feed.

**How do I create a post?**
From the feed page, click "Creer un article". Select a topic, fill in the title and content, then click "Creer". You will be redirected to your new post.

**How do I update my profile?**
Go to the Profile page. You can change your username, email or password independently. Leave the password field empty to keep your current one. Click "Sauvegarder" to apply changes.

**How do I unsubscribe from a topic?**
Go to the Profile page. Your subscriptions are listed at the bottom. Click "Se desabonner" on the topic you want to remove.

**How do I comment on a post?**
Open a post from the feed. Type your comment in the text area at the bottom and click the send button.

## Common errors and solutions

**`Access denied for user` when starting the back-end**
Your MySQL credentials are incorrect. Check the `DATABASE_USERNAME` and `DATABASE_PASSWORD` values in your `back/.env` file.

**`app.jwt.secret` placeholder not resolved**
The `JWT_SECRET` environment variable is missing. Add it to your `back/.env` file or set it as an environment variable.

**Port 3001 already in use**
Another process is using port 3001. Either stop that process or change the port with `SERVER_PORT=3002` in your `.env` file.

**`CORS` error in the browser console**
The front-end must run on `http://localhost:4200`. If you use a different port, the back-end will reject requests. Update the CORS config in `SecurityConfig.java` if needed.

**Login fails with valid credentials**
Make sure you are using either your email or username in the login field, not both. The password is case-sensitive and must match exactly.

**Empty feed after login**
Your feed only shows posts from topics you're subscribed to. Go to the Topics page and subscribe to at least one topic.

**`401 Unauthorized` on API requests**
Your JWT token has expired (valid for 24h) or is missing. Log in again to get a new token. Make sure the `Authorization: Bearer <token>` header is included in your requests.

**Back-end tests fail with `Access denied`**
Set the test database credentials via environment variables:
```bash
DB_TEST_USERNAME=your_user DB_TEST_PASSWORD=your_password mvn test
```

**Cypress tests fail**
Make sure both the back-end (`localhost:3001`) and front-end (`localhost:4200`) are running before executing `npm run e2e`. The seed data (Flyway V3 migration) must also be present in the database.

**`Flyway migration checksum mismatch`**
This happens when a migration file was modified after being applied. Either reset the database (`DROP DATABASE mdd; DROP DATABASE mdd_test;`) or run Flyway repair:
```bash
mvn flyway:repair
```
