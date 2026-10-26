<#import "layout.ftlh" as l>
<@l.page title="About" active="about">
<h1>About this app</h1>

  <p>Plant Tracker demo. The same database and the same service layer power <strong>two front ends</strong>:
     a JSON API for programs, and the HTML pages you are looking at now for people.</p>

  <img class="hero" src="/thumbnails/plant-placeholder.svg" alt="Illustration of a potted plant">

  <h2>How a page request flows</h2>
  <ol>
    <li>The browser requests a URL, for example <code>/plants/3</code>.</li>
    <li>A <code>@Controller</code> method runs, asks the service for data, and puts it in the <code>Model</code>.</li>
    <li>The controller returns a view name. Spring renders the matching FreeMarker template.</li>
    <li>The browser receives HTML.</li>
  </ol>

  <h2>Two front ends, one backend</h2>
  <table>
    <thead><tr><th></th><th>Web pages</th><th>JSON API</th></tr></thead>
    <tbody>
      <tr><td>Controller</td><td><code>PlantUiController</code></td><td><code>PlantController</code></td></tr>
      <tr><td>URLs</td><td><code>/</code>, <code>/plants/...</code>, <code>/about</code></td><td><code>/api/plants/...</code></td></tr>
      <tr><td>Returns</td><td>HTML from templates</td><td>JSON</td></tr>
      <tr><td>Calls</td><td colspan="2">the same <code>PlantService</code> and <code>PlantRepository</code></td></tr>
    </tbody>
  </table>

  <p>Pictures are saved in a static directory.</p>

</@l.page>
