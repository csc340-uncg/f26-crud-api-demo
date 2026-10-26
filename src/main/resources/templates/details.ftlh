<#import "layout.ftlh" as l>
  <@l.page title="Plant Details" active="home">
    <p><a href="/">Back to all plants</a></p>

    <div class="details">
      <img class="hero" src="/thumbnails/${(plant.thumbnailUrl)!'plant-placeholder.svg'}" alt="${plant.name}" width="400" height="400">
      <div>
        <h1>${plant.name}</h1>
        <dl>
          <dt>Light needs</dt>
          <dd>${plant.lightNeeds?lower_case?cap_first}</dd>
          <dt>Last watered</dt>
          <dd>${plant.lastWatered}</dd>
          <dt>Watering interval</dt>
          <dd>Every ${plant.wateringIntervalDays?c} days</dd>
          <dt>Toxic to pets</dt>
          <dd>${plant.toxicToPets?then('Yes', 'No')}</dd>
        </dl>

        <div class="actions">
          <a class="button" href="/plants/${plant.id?c}/edit">Edit</a>
          <#-- HTML forms can only send GET and POST, so delete is a POST to its own URL. -->
            <form method="post" action="/plants/${plant.id?c}/delete" onsubmit="return confirm('Remove this plant?');">
              <button type="submit" class="danger">Delete</button>
            </form>
        </div>
      </div>
    </div>

  </@l.page>