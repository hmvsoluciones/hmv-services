<template>
  <div class="error-page">
    <div class="error-container">
      <div class="error-image">
        <img src="/content/images/custom/logo-hmv.png" alt="Logo HMV" />
      </div>
      <div class="error-content">
        <h1>¡Ups! Algo salió mal</h1>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <p v-if="error403" class="error-message">No tiene permisos para acceder a esta página.</p>
        <p v-if="error404" class="error-message">La página que buscas no existe.</p>
        <button @click="goHome" class="btn-home">Volver al inicio</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./error.component.ts"></script>

<style scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #e0e7ff, #c7d2fe);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 20px;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  background: #ffffffcc;
  padding: 50px 35px;
  border-radius: 20px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
  max-width: 650px;
  width: 100%;
  transition: transform 0.3s ease;
}

.error-container:hover {
  transform: translateY(-5px);
}

.error-image img {
  max-width: 180px;
  margin-bottom: 25px;
}

.error-content h1 {
  font-size: 2.5rem;
  margin-bottom: 20px;
  color: #1e293b;
}

.error-message {
  font-size: 1.1rem;
  margin-bottom: 25px;
  color: #475569;
}

.btn-home {
  padding: 12px 30px;
  background-color: #6366f1;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-home:hover {
  background-color: #4f46e5;
  transform: scale(1.05);
}

@media (min-width: 768px) {
  .error-container {
    flex-direction: row;
    text-align: left;
  }

  .error-image {
    margin-right: 40px;
  }

  .error-content h1 {
    font-size: 3rem;
  }

  .error-message {
    font-size: 1.2rem;
  }
}
</style>
