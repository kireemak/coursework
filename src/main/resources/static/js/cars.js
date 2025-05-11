async function loadCars() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/cars', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status === 401) {
            localStorage.removeItem('jwtToken');
            window.location.href = 'login.html';
            return;
        }

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const cars = await response.json();

        const tableBody = document.getElementById('carsTable').querySelector('tbody');
        tableBody.innerHTML = '';

        cars.forEach(car => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${car.id}</td>
                <td>${car.brand}</td>
                <td>${car.model}</td>
                <td>${car.year}</td>
                <td>$${car.rentalPrice}</td>
                <td>${car.status}</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading cars:', error);
    }
}

document.addEventListener('DOMContentLoaded', loadCars);