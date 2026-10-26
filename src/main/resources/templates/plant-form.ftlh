<#import "layout.ftlh" as l>
  <@l.page title=formTitle active=(plantId??)?then('', 'new' )>
    <h1>${formTitle}</h1>
    <form method="post" action="${formAction}" enctype="multipart/form-data" class="form">

      <label for="name">Name</label>
      <input id="name" name="name" type="text" value="${(plant.name)!''}" required>

      <label for="lightNeeds">Light needs</label>
      <#assign current=(plant.lightNeeds?string)!''>
        <select id="lightNeeds" name="lightNeeds" required>
          <option value="">Choose...</option>
          <option value="LOW" <#if current=='LOW'>selected</#if>>Low</option>
          <option value="MEDIUM" <#if current=='MEDIUM'>selected</#if>>Medium</option>
          <option value="BRIGHT" <#if current=='BRIGHT'>selected</#if>>Bright</option>
        </select>

        <label for="lastWatered">Last watered</label>
        <input id="lastWatered" name="lastWatered" type="date" value="${(plant.lastWatered)!''}">

        <label for="wateringIntervalDays">Watering interval (days)</label>
        <input id="wateringIntervalDays" name="wateringIntervalDays" type="number" min="1" max="365"
          value="${(plant.wateringIntervalDays?c)!''}">

        <label class="check">
          <input type="checkbox" name="toxicToPets" <#if plant.toxicToPets>checked</#if>>
          Toxic to pets
        </label>

        <label for="imageFile">Picture (JPEG, PNG, or WebP, up to 1 MB)</label>
        <#if plantId??>
          <img class="thumb" src="/plants/${plantId?c}/image" alt="Current picture">
        </#if>
        <input id="imageFile" name="imageFile" type="file" accept="image/jpeg,image/png,image/webp">

        <div class="actions">
          <button type="submit">Save</button>
          <#if plantId??>
            <a href="/plants/${plantId?c}">Cancel</a>
            <#else>
              <a href="/">Cancel</a>
          </#if>
        </div>
    </form>

  </@l.page>