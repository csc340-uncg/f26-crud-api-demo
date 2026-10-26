<#import "layout.ftlh" as l>
  <@l.page title="Error" active="home">
    <h1>Error</h1>
    <p>${errorMessage!'An error occurred.'} </p>
    <a href="/">Back to all plants</a>

  </@l.page>