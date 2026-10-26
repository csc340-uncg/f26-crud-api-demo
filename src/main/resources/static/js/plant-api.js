export const API = '/api/plants';

export async function request(url, options = {}) {
  const response = await fetch(url, options);
  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try {
      const body = await response.json();
      message = body.message || body.detail || body.error || message;
    } catch (_) { /* Error responses may have no JSON body. */ }
    throw new Error(message);
  }
  return response.status === 204 ? null : response.json();
}

export function showError(error) {
  const status = document.querySelector('#status');
  status.textContent = error.message || 'Something went wrong. Please try again.';
  status.className = 'error';
}

export function imageUrl(plant) {
  const file = plant.thumbnailUrl || 'plant-placeholder.svg';
  return `/thumbnails/${encodeURIComponent(file.split('/').pop())}`;
}

export function addText(parent, tag, text, className) {
  const element = document.createElement(tag);
  element.textContent = text;
  if (className) element.className = className;
  parent.append(element);
  return element;
}

export function formatLight(lightNeeds) {
  return lightNeeds.toLowerCase().replace(/^./, (letter) => letter.toUpperCase());
}

export function currentPlantId() {
  return new URLSearchParams(window.location.search).get('id');
}
