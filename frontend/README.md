# Microshop Frontend

Frontend React para la plataforma Microshop Platform.

## 🚀 Inicio Rápido

### Instalación

```bash
npm install
```

### Desarrollo

```bash
npm run dev
```

La aplicación estará disponible en `http://localhost:3000`

### Build para Producción

```bash
npm run build
```

## 📁 Estructura del Proyecto

```
frontend/
├── src/
│   ├── components/          # Componentes reutilizables
│   │   └── layout/         # Layout, Navbar, Footer
│   ├── pages/              # Páginas de la aplicación
│   │   ├── Home.jsx
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── Products.jsx
│   │   └── AdminPanel.jsx
│   ├── App.jsx             # Componente principal con rutas
│   ├── main.jsx            # Punto de entrada
│   └── index.css           # Estilos globales
├── public/                 # Archivos estáticos
├── package.json
├── vite.config.js
└── index.html
```

## 🛣️ Rutas

- `/` - Página de inicio
- `/login` - Inicio de sesión
- `/register` - Registro de usuarios
- `/products` - Catálogo de productos
- `/admin` - Panel de administración (requiere rol ADMIN)

## 🔧 Tecnologías

- **React 18** - Biblioteca UI
- **React Router v6** - Enrutamiento
- **Vite** - Build tool y dev server
- **Axios** - Cliente HTTP

## 📝 Notas

- El frontend se conecta al API Gateway en `http://localhost:8080`
- Los tokens JWT se almacenan en `localStorage`
- El proxy de Vite está configurado para redirigir `/api` al backend

