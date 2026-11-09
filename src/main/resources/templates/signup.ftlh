<#import "layout.ftlh" as l>
  <@l.page title="Register" active="home">

  <div class="container" style="margin: 20px;">
    <h1>Register</h1>
    <form action="/signup" method="post">
      <div class="mb-3">
        <label for="username" class="form-label">Username</label>
        <input type="text" class="form-control" id="username" name="username" required>
      </div>
      <div class="mb-3">
        <label for="name" class="form-label">Name</label>
        <input type="text" class="form-control" id="name" name="name" required>
      </div>
      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" id="password" name="password" required>
      </div>
      <div class="mb-3">
        <label for="role" class="form-label">Role</label>
        <select class="form-select" id="role" name="role" required>
          <option value="">Select an option</option>
          <option value="PLANT_PARENT">Plant Parent</option>
          <option value="ADMIN">Admin</option>
          <option value="GUEST">Guest</option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Register</button>
    </form>
  </div>
  </@l.page>