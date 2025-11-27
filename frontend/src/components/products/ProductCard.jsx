import { Link } from 'react-router-dom'

/**
 * Componente de tarjeta de producto con diseño mejorado usando Tailwind.
 * 
 * @param {Object} props - Propiedades del componente
 * @param {Object} props.product - Objeto con los datos del producto
 * @returns {JSX.Element} Tarjeta de producto
 */
function ProductCard({ product }) {
  const formatPrice = (price) => {
    return new Intl.NumberFormat('es-ES', {
      style: 'currency',
      currency: 'EUR'
    }).format(price)
  }

  const getStockBadge = (stock) => {
    if (stock === 0) {
      return (
        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
          Sin stock
        </span>
      )
    }
    if (stock < 5) {
      return (
        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
          Últimas unidades
        </span>
      )
    }
    return (
      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
        Disponible
      </span>
    )
  }

  return (
    <Link to={`/products/${product.id}`} className="block h-full">
      <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300 h-full flex flex-col">
        {/* Imagen del producto */}
        <div className="relative h-48 bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center">
          <span className="text-6xl">🛍️</span>
          {product.stock === 0 && (
            <div className="absolute top-2 right-2 bg-red-600 text-white px-3 py-1 rounded-full text-xs font-bold">
              Agotado
            </div>
          )}
        </div>

        {/* Contenido */}
        <div className="p-5 flex-1 flex flex-col">
          {/* Categoría */}
          <div className="mb-2">
            <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-gray-100 text-gray-800">
              {product.category}
            </span>
          </div>

          {/* Nombre */}
          <h3 className="text-lg font-semibold text-gray-900 mb-2 line-clamp-2">
            {product.name}
          </h3>

          {/* Descripción */}
          {product.description && (
            <p className="text-sm text-gray-600 mb-4 line-clamp-2 flex-1">
              {product.description}
            </p>
          )}

          {/* Footer con precio y stock */}
          <div className="mt-auto pt-4 border-t border-gray-200">
            <div className="flex items-center justify-between mb-3">
              <span className="text-2xl font-bold text-blue-600">
                {formatPrice(product.price)}
              </span>
              {getStockBadge(product.stock)}
            </div>

            {product.stock > 0 && (
              <button className="w-full btn-primary text-sm py-2">
                Ver Detalle
              </button>
            )}
          </div>
        </div>
      </div>
    </Link>
  )
}

export default ProductCard
