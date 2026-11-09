<#import "layout.ftlh" as l>
  <@l.page title="Login" active="home">
  <div class="container" style="margin: 20px;">
    <h1>Login</h1>
    <form action="/login" method="post">
      <div class="mb-3">
        <label for="username" class="form-label">Username</label>
        <input type="text" class="form-control" id="username" name="username" required>
      </div>
      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" id="password" name="password" required>
      </div>
      <button type="submit" class="btn btn-primary">Login</button>
    </form>
    <p>Don't have an account?<a href="/signup" class="btn btn-link"> Sign up</a></p>
  </div>
  </@l.page>
