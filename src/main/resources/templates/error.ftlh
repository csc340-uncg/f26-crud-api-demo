<#import "layout.ftlh" as l>
  <@l.page title="My Plants" active="home">
  <div class="container" style="margin-top: 15px;">
    <h1>Error Page</h1>

    <div class="alert alert-danger" role="alert">
      <h4 class="alert-heading">Uh Oh!</h4>
      <hr>
      <p class="mb-0">Error Status: ${status!""}</p>
      <p class="mb-0">Message: ${error!""}</p>
    </div>
  </div>
  </@l.page>