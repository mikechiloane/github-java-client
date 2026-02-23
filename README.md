
# Hey there! Welcome to the GitHub Java Client! 🚀

This is a simple, no-fuss Java client for the GitHub API. We built this to make it super easy to interact with GitHub without having to deal with the nitty-gritty of HTTP requests. Think of it as your friendly neighborhood wrapper for the GitHub API.

## What's inside?

This client helps you to:

-   Talk to the GitHub API in a straightforward way.
-   Automatically handle API rate limits, so you don't have to.
-   Perform common actions like creating repositories, managing files, and fetching user information.

## Getting Started is Easy!

To get started, you'll need to add this project as a dependency. Since this is a Maven project, you can add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>com.recceda</groupId>
    <artifactId>github-java-client</artifactId>
    <version>1.1.9</version>
</dependency>
```

Then, you can start using the client in your code. Here's a quick example of how to get user information:

```java
import com.recceda.http.github.GithubClient;
import com.recceda.action.UserAction;

public class Main {
    public static void main(String[] args) {
        // First, create a new client with your GitHub token
        GithubClient client = new GithubClient("your-github-token");

        // Then, create an action and use it to fetch data
        UserAction userAction = new UserAction(client);
        var user = userAction.getUser("your-username").join();

        // And that's it!
        System.out.println("User: " + user.getName());
    }
}
```

## Don't worry about Rate Limits!

We all know that hitting API rate limits can be a pain. That's why this client comes with built-in rate limit handling. Here's what it does for you:

-   **Tracks your API usage**: It keeps an eye on your remaining requests.
-   **Prevents you from hitting the limit**: If you're about to exceed your limit, the client will pause and wait for the limit to reset.
-   **Warns you**: When you're running low on requests, it will let you know.

You can also customize how it handles rate limits. For more details, check out our [Rate Limit Guide](RATE_LIMIT_GUIDE.md).

## Want to contribute?

Awesome! We'd love to have you. If you want to contribute, here's how you can do it:

1.  Fork this repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes and write some tests.
4.  Open a pull request and describe your changes.

We're excited to see what you come up with!

---

Happy coding! 😊
