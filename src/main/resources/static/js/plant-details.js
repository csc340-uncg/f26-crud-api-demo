import { API, addText, currentPlantId, formatLight, imageUrl, request, showError } from './plant-api.js';

async function renderDetails() {
  const section = document.querySelector('#plant-details');
  const id = currentPlantId();
  if (!id) return showError(new Error('No plant ID was provided.'));

  try {
    const plant = await request(`${API}/${encodeURIComponent(id)}`);
    section.replaceChildren();

    const image = document.createElement('img');
    image.className = 'hero';
    image.src = imageUrl(plant);
    image.alt = plant.name;

    const content = document.createElement('div');
    addText(content, 'h1', plant.name);
    const list = document.createElement('dl');
    [
      ['Light needs', formatLight(plant.lightNeeds)],
      ['Last watered', plant.lastWatered],
      ['Watering interval', `Every ${plant.wateringIntervalDays} days`],
      ['Toxic to pets', plant.toxicToPets ? 'Yes' : 'No']
    ].forEach(([label, value]) => {
      addText(list, 'dt', label);
      addText(list, 'dd', value);
    });
    content.append(list);

    const actions = document.createElement('div');
    actions.className = 'actions';
    const edit = document.createElement('a');
    edit.className = 'button';
    edit.href = `/fetch/form.html?id=${encodeURIComponent(plant.id)}`;
    edit.textContent = 'Edit';
    actions.append(edit);

    const remove = document.createElement('button');
    remove.type = 'button';
    remove.className = 'danger';
    remove.textContent = 'Delete';
    remove.addEventListener('click', async () => {
      if (!window.confirm('Remove this plant?')) return;
      try {
        await request(`${API}/${encodeURIComponent(plant.id)}`, { method: 'DELETE' });
        window.location.href = '/fetch/index.html';
      } catch (error) {
        showError(error);
      }
    });
    actions.append(remove);
    content.append(actions);
    section.append(image, content);
    section.hidden = false;
  } catch (error) {
    showError(error);
  }
}

renderDetails();
