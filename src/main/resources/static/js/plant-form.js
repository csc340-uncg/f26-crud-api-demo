import { API, currentPlantId, request, showError } from './plant-api.js';

async function renderForm() {
  const form = document.querySelector('#plant-form');
  const id = currentPlantId();
  const editing = Boolean(id);
  const today = new Date();
  document.querySelector('#lastWatered').value = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;

  if (editing) {
    document.querySelector('#form-title').textContent = 'Edit Plant';
    document.querySelector('#save-button').textContent = 'Save';
    document.querySelector('#cancel-link').href = `/fetch/details.html?id=${encodeURIComponent(id)}`;
    try {
      const plant = await request(`${API}/${encodeURIComponent(id)}`);
      form.elements.name.value = plant.name;
      form.elements.lightNeeds.value = plant.lightNeeds;
      form.elements.lastWatered.value = plant.lastWatered;
      form.elements.wateringIntervalDays.value = plant.wateringIntervalDays;
      form.elements.toxicToPets.checked = plant.toxicToPets;
    } catch (error) {
      showError(error);
    }
  }

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    const payload = {
      name: form.elements.name.value.trim(),
      lightNeeds: form.elements.lightNeeds.value,
      lastWatered: form.elements.lastWatered.value,
      wateringIntervalDays: Number(form.elements.wateringIntervalDays.value),
      toxicToPets: form.elements.toxicToPets.checked
    };
    try {
      const plant = await request(editing ? `${API}/${encodeURIComponent(id)}` : API, {
        method: editing ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      window.location.href = `/fetch/details.html?id=${encodeURIComponent(plant.id)}`;
    } catch (error) {
      showError(error);
    }
  });
}

renderForm();
