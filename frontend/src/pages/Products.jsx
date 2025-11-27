import { useState, useEffect } from 'react'
import productService from '../services/api/productService'
import ProductList from '../components/products/ProductList'

/**
 * Página de listado de productos con diseño mejorado.
 * 
 * @returns {JSX.Element} Página de productos
 */
function Products() {
  const [products, setProducts] = useState([])
  const [filteredProducts, setFilteredProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [category, setCategory] = useState('')
  const [searchTerm, setSearchTerm] = useState('')

  useEffect(() => {
    fetchProducts()
  }, [category])

  useEffect(() => {
    filterProducts()
  }, [searchTerm, products])

  const fetchProducts = async () => {
    try {
      setLoading(true)
      setError('')
      
      let productsData
      if (category) {
        productsData = await productService.getProductsByCategory(category)
      } else {
        productsData = await productService.getProducts()
      }
      
      setProducts(productsData)
    } catch (err) {
      setError(err.message || 'Error al cargar los productos')
      setProducts([])
    } finally {
      setLoading(false)
    }
  }

  const filterProducts = () => {
    if (!searchTerm.trim()) {
      setFilteredProducts(products)
      return
    }

    const filtered = products.filter(product =>
      product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      product.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      product.category?.toLowerCase().includes(searchTerm.toLowerCase())
    )
    
    setFilteredProducts(filtered)
  }

  const handleCategoryChange = (e) => {
    setCategory(e.target.value)
    setSearchTerm('')
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">
            Catálogo de Productos
          </h1>
          <p className="text-gray-600">
            Explora nuestra amplia selección de productos
          </p>
        </div>

        {/* Filtros */}
        <div className="card mb-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-2">
                Buscar productos
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg className="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                  </svg>
                </div>
                <input
                  type="text"
                  id="search"
                  placeholder="Buscar por nombre, descripción o categoría..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="input-field pl-10"
                />
              </div>
            </div>

            <div className="md:w-64">
              <label htmlFor="category" className="block text-sm font-medium text-gray-700 mb-2">
                Filtrar por categoría
              </label>
              <select
                id="category"
                value={category}
                onChange={handleCategoryChange}
                className="input-field"
              >
                <option value="">Todas las categorías</option>
                <option value="Electrónica">Electrónica</option>
                <option value="Ropa">Ropa</option>
                <option value="Hogar">Hogar</option>
                <option value="Deportes">Deportes</option>
              </select>
            </div>
          </div>

          {category && (
            <div className="mt-4 flex items-center justify-between p-3 bg-blue-50 border border-blue-200 rounded-lg">
              <span className="text-sm text-blue-800">
                Mostrando productos de: <strong>{category}</strong>
              </span>
              <button
                onClick={() => setCategory('')}
                className="text-sm text-blue-600 hover:text-blue-800 font-medium"
              >
                Limpiar filtro
              </button>
            </div>
          )}
        </div>

        {/* Lista de productos */}
        <ProductList 
          products={filteredProducts}
          loading={loading}
          error={error}
        />
      </div>
    </div>
  )
}

export default Products
