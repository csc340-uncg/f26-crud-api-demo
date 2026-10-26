import { API, addText, formatLight, imageUrl, request, showError } from './plant-api.js';

async function renderGallery() {
  const grid = document.querySelector('#plant-grid');
  const currentSearch = new URLSearchParams(window.location.search);
  const name = currentSearch.get('name') || '';
  const light = currentSearch.get('light') || '';
  const params = new URLSearchParams();

  document.querySelector('#search-name').value = name;
  document.querySelector('#search-light').value = light;
  if (name.trim()) params.set('name', name.trim());
  if (light) params.set('light', light);

  document.querySelector('#search-form').addEventListener('submit', (event) => {
    event.preventDefault();
    const query = new URLSearchParams();
    const searchName = document.querySelector('#search-name').value.trim();
    const searchLight = document.querySelector('#search-light').value;
    if (searchName) query.set('name', searchName);
    if (searchLight) query.set('light', searchLight);
    window.location.search = query.toString();
  });
  document.querySelector('#clear-search').addEventListener('click', () => {
    window.location.href = '/fetch/index.html';
  });

  try {
    const plants = await request(`${API}${params.size ? `?${params}` : ''}`);
    document.querySelector('#count').textContent = `${plants.length} plant(s)`;
    if (!plants.length) return addText(grid, 'p', 'No plants found.', 'empty');

    plants.forEach((plant) => {
      const card = document.createElement('article');
      card.className = 'card';
      const link = document.createElement('a');
      link.href = `/fetch/details.html?id=${encodeURIComponent(plant.id)}`;
      const image = document.createElement('img');
      image.src = imageUrl(plant);
      image.alt = plant.name;
      image.width = 200;
      image.height = 200;
      link.append(image);
      card.append(link);

      const body = document.createElement('div');
      body.className = 'card-body';
      const heading = document.createElement('h3');
      const nameLink = document.createElement('a');
      nameLink.href = link.href;
      nameLink.textContent = plant.name;
      heading.append(nameLink);
      body.append(heading);
      addText(body, 'p', `Light: ${formatLight(plant.lightNeeds)}`, 'meta');
      addText(body, 'p', `Last watered ${plant.lastWatered}, every ${plant.wateringIntervalDays} days`, 'meta');
      if (plant.toxicToPets) addText(body, 'span', 'Toxic to pets', 'badge');
      card.append(body);
      grid.append(card);
    });
  } catch (error) {
    showError(error);
  }
}

renderGallery();
