<template>
  <div>
    <!-- Overlay para fondo oscuro -->
    <div v-if="visible" class="sidebar-overlay" @click="closeSidebar"></div>

    <!-- Sidebar -->
    <div class="sidebar" :class="{ 'sidebar-open': visible }">
      <!-- Header -->
      <div class="sidebar-header">
        <div class="d-flex align-items-center justify-content-between p-3 border-bottom">
          <strong class="sidebar-title">{{ PRODUCT_NAME }}</strong>
          <button class="btn btn-outline-secondary btn-sm sidebar-close-btn" @click="closeSidebar">
            <font-awesome-icon icon="times" />
          </button>
        </div>
      </div>

      <!-- Contenido -->
      <div class="sidebar-content">
        <nav class="sidebar-nav">
          <div class="nav flex-column">
            <!-- Grupo: Navegación Principal -->
            <div class="sidebar-group">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Navegación</small>
              </div>
              <button class="nav-link sidebar-nav-item" @click="handleNavigation($event, '/')" exact>
                <font-awesome-icon icon="home" class="sidebar-icon" />
                <span class="sidebar-text">Inicio</span>
              </button>
              <button class="nav-link sidebar-nav-item" @click="handleLoginClick" exact>
                <font-awesome-icon icon="sign-in-alt" />
                <span>Iniciar sesión</span>
              </button>
            </div>
            <!-- Grupo de Catalogos-->
            <div class="sidebar-group" v-if="isAuthenticated">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Cat&aacute;logos</small>
              </div>
              <router-link to="/cliente" class="nav-link sidebar-nav-item" @click="closeSidebar" exact>
                <font-awesome-icon icon="user" class="sidebar-icon" />
                <span class="sidebar-text">Clientes</span>
              </router-link>
              <router-link to="/tipo-producto" class="nav-link sidebar-nav-item" @click="closeSidebar" exact>
                <font-awesome-icon icon="burn" class="sidebar-icon" />
                <span class="sidebar-text">Tipos de productos</span>
              </router-link>
            </div>
            <!-- Grupo de Productos-->
            <div class="sidebar-group" v-if="isAuthenticated">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Productos</small>
              </div>
              <router-link to="/producto" class="nav-link sidebar-nav-item" @click="closeSidebar" exact>
                <font-awesome-icon icon="binoculars" class="sidebar-icon" />
                <span class="sidebar-text">Productos</span>
              </router-link>
              <router-link to="/plan" class="nav-link sidebar-nav-item" @click="closeSidebar" exact>
                <font-awesome-icon icon="money-bill" class="sidebar-icon" />
                <span class="sidebar-text">Planes</span>
              </router-link>
            </div>

            <!-- Grupo de Suscripciones-->
            <div class="sidebar-group" v-if="isAuthenticated">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Suscripciones</small>
              </div>
              <router-link to="/suscripcion" class="nav-link sidebar-nav-item" @click="closeSidebar" exact>
                <font-awesome-icon icon="binoculars" class="sidebar-icon" />
                <span class="sidebar-text">Suscripciones</span>
              </router-link>
              <router-link to="/pago" class="nav-link sidebar-nav-item" @click="closeSidebar" exact>
                <font-awesome-icon icon="money-bills" class="sidebar-icon" />
                <span class="sidebar-text">Pagos</span>
              </router-link>
            </div>

            <!-- Grupo: Administración (ejemplo condicional) -->
            <div class="sidebar-group" v-if="showAdminSection">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Administración</small>
              </div>
              <router-link to="/admin/user-management" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="user-cog" class="sidebar-icon" />
                <span class="sidebar-text">Gestión Usuarios</span>
              </router-link>
              <router-link to="/admin/metrics" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="tachometer-alt" />
                <span class="sidebar-text">M&eacute;tricas</span>
              </router-link>
              <router-link to="/admin/health" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="heart" />
                <span class="sidebar-text">M&eacute;tricas</span>
              </router-link>
              <router-link to="/admin/configuration" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="cog" />
                <span class="sidebar-text">Configuración</span>
              </router-link>
              <router-link to="/admin/logs" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="tasks" />
                <span class="sidebar-text">Logs</span>
              </router-link>
              <router-link to="/admin/docs" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="book" />
                <span class="sidebar-text">API</span>
              </router-link>
            </div>

            <!-- Grupo: Configuración (ejemplo condicional) -->
            <div class="sidebar-group" v-if="isAuthenticated">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Mi Cuenta</small>
              </div>
              <router-link to="/account/settings" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="wrench" class="sidebar-icon" />
                <span class="sidebar-text">Perfil</span>
              </router-link>
              <router-link to="/account/password" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="cogs" class="sidebar-icon" />
                <span class="sidebar-text">Contrase&ntilde;a</span>
              </router-link>
              <router-link to="/register" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="user-plus" />
                <span class="sidebar-text">Crear cuenta</span>
              </router-link>
              <a href="#" class="nav-link sidebar-nav-item" @click="handleLogout">
                <font-awesome-icon icon="sign-out-alt" class="sidebar-icon" />
                <span class="sidebar-text">Cerrar Sesión</span>
              </a>
            </div>
          </div>
        </nav>
      </div>

      <!-- Footer -->
      <div class="sidebar-footer p-3 border-top">
        <small class="text-muted d-block text-center"> &copy; {{ new Date().getFullYear() }} {{ PRODUCT_NAME }} </small>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./sidebar.component.ts"></script>

<style scoped lang="scss" src="./sidebar.component.scss"></style>
