import { Link } from 'react-router-dom'

/**
 * Página de inicio con diseño mejorado usando Tailwind CSS.
 * 
 * @returns {JSX.Element} Página de inicio
 */
function Home() {
  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-blue-600 to-indigo-700 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
          <div className="text-center">
            <h1 className="text-5xl md:text-6xl font-extrabold mb-6">
              Bienvenido a Microshop
            </h1>
            <p className="text-xl md:text-2xl mb-8 text-blue-100">
              Tu plataforma de e-commerce de confianza
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link to="/products" className="btn-primary text-lg px-8 py-3 inline-flex items-center justify-center">
                Ver Productos
              </Link>
              <Link to="/register" className="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold text-lg hover:bg-gray-100 transition-colors inline-flex items-center justify-center">
                Crear Cuenta
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-4xl font-bold text-center text-gray-900 mb-12">
            Características
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center p-8 rounded-lg hover:shadow-lg transition-shadow">
              <div className="text-6xl mb-4">🛍️</div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Catálogo Completo</h3>
              <p className="text-gray-600">
                Explora nuestra amplia gama de productos de alta calidad
              </p>
            </div>
            <div className="text-center p-8 rounded-lg hover:shadow-lg transition-shadow">
              <div className="text-6xl mb-4">🔒</div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Seguro y Confiable</h3>
              <p className="text-gray-600">
                Protección de datos y transacciones seguras
              </p>
            </div>
            <div className="text-center p-8 rounded-lg hover:shadow-lg transition-shadow">
              <div className="text-6xl mb-4">⚡</div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Rápido y Eficiente</h3>
              <p className="text-gray-600">
                Plataforma optimizada para la mejor experiencia
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  )
}

export default Home
