import ProductCard from './ProductCard'

/**
 * Componente que muestra una lista de productos en formato de grid.
 * 
 * @param {Object} props - Propiedades del componente
 * @param {Array} props.products - Array de productos a mostrar
 * @param {boolean} props.loading - Indica si está cargando
 * @param {string} props.error - Mensaje de error (si existe)
 * @returns {JSX.Element} Lista de productos
 */
function ProductList({ products, loading, error }) {
  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center py-20">
        <div className="spinner w-12 h-12 mb-4"></div>
        <p className="text-gray-600 text-lg">Cargando productos...</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center py-20">
        <div className="text-6xl mb-4">⚠️</div>
        <h3 className="text-xl font-semibold text-gray-900 mb-2">Error al cargar productos</h3>
        <p className="text-gray-600">{error}</p>
      </div>
    )
  }

  if (!products || products.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-20">
        <div className="text-6xl mb-4">📦</div>
        <h3 className="text-xl font-semibold text-gray-900 mb-2">No se encontraron productos</h3>
        <p className="text-gray-600">
          Intenta cambiar los filtros de búsqueda
        </p>
      </div>
    )
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      {products.map((product) => (
        <ProductCard key={product.id} product={product} />
      ))}
    </div>
  )
}

export default ProductList
