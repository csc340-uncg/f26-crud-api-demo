<#import "layout.ftlh" as l>
  <@l.page title="My Plants" active="home">

    <h1>My Plants</h1>

    <form method="get" action="/plants" class="search">
      <input type="text" name="name" value="${name!''}" placeholder="Search by name">
      <select name="light">
        <option value="">Any light</option>
        <option value="LOW" <#if selectedLight=='LOW'>selected</#if>>Low</option>
        <option value="MEDIUM" <#if selectedLight=='MEDIUM'>selected</#if>>Medium</option>
        <option value="BRIGHT" <#if selectedLight=='BRIGHT'>selected</#if>>Bright</option>
      </select>
      <button type="submit">Search</button>
      <#if name?has_content || selectedLight?has_content>
        <a href="/plants" class="clear">Clear</a>
      </#if>
    </form>

    <p class="count">${plants?size} plant(s)</p>

    <div class="grid">
      <#list plants as plant>
        <article class="card">
          <a href="/plants/${plant.id?c}">
            <img src="/thumbnails/${(plant.thumbnailUrl)!'plant-placeholder.svg'}" alt="${plant.name}" width="200"
              height="200">
          </a>
          <div class="card-body">
            <h3><a href="/plants/${plant.id?c}">${plant.name}</a></h3>
            <p class="meta">Light: ${plant.lightNeeds?lower_case?cap_first}</p>
            <p class="meta">Last watered ${plant.lastWatered}, every ${plant.wateringIntervalDays?c} days</p>
            <#if plant.toxicToPets>
              <span class="badge">Toxic to pets</span>
            </#if>
          </div>
        </article>
        <#else>
          <p class="empty">No plants found.</p>
      </#list>
    </div>
  </@l.page>