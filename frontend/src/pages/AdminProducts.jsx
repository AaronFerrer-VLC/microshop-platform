import { useState, useEffect } from 'react'
import { useAuth } from '../contexts/AuthContext'
import productService from '../services/api/productService'
import ProductTable from '../components/admin/ProductTable'
import ProductForm from '../components/admin/ProductForm'

/**
 * Página de administración de productos con diseño mejorado.
 * 
 * @returns {JSX.Element} Página de administración de productos
 */
function AdminProducts() {
  const { token } = useAuth()
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingProduct, setEditingProduct] = useState(null)
  const [formLoading, setFormLoading] = useState(false)

  useEffect(() => {
    fetchProducts()
  }, [token])

  const fetchProducts = async () => {
    try {
      setLoading(true)
      setError('')
      const productsData = await productService.getProducts(token)
      setProducts(productsData)
    } catch (err) {
      setError(err.message || 'Error al cargar los productos')
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = () => {
    setEditingProduct(null)
    setShowForm(true)
    setError('')
    setSuccess('')
  }

  const handleEdit = (product) => {
    setEditingProduct(product)
    setShowForm(true)
    setError('')
    setSuccess('')
  }

  const handleSubmit = async (productData) => {
    try {
      setFormLoading(true)
      setError('')
      setSuccess('')

      if (editingProduct) {
        await productService.updateProduct(editingProduct.id, productData, token)
        setSuccess('Producto actualizado exitosamente')
      } else {
        await productService.createProduct(productData, token)
        setSuccess('Producto creado exitosamente')
      }

      await fetchProducts()
      
      setTimeout(() => {
        setShowForm(false)
        setEditingProduct(null)
        setSuccess('')
      }, 1500)
    } catch (err) {
      setError(err.message || 'Error al guardar el producto')
    } finally {
      setFormLoading(false)
    }
  }

  const handleCancel = () => {
    setShowForm(false)
    setEditingProduct(null)
    setError('')
    setSuccess('')
  }

  const handleDelete = async (productId, productName) => {
    if (!window.confirm(`¿Estás seguro de que deseas eliminar el producto "${productName}"? Esta acción no se puede deshacer.`)) {
      return
    }

    try {
      setError('')
      setSuccess('')
      await productService.deleteProduct(productId, token)
      setSuccess('Producto eliminado exitosamente')
      await fetchProducts()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(err.message || 'Error al eliminar el producto')
    }
  }

  if (showForm) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        {error && (
          <div className="max-w-4xl mx-auto px-4 mb-4">
            <div className="alert alert-error">
              <div className="flex items-center">
                <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                </svg>
                {error}
              </div>
            </div>
          </div>
        )}
        {success && (
          <div className="max-w-4xl mx-auto px-4 mb-4">
            <div className="alert alert-success">
              <div className="flex items-center">
                <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                </svg>
                {success}
              </div>
            </div>
          </div>
        )}
        <ProductForm
          product={editingProduct}
          onSubmit={handleSubmit}
          onCancel={handleCancel}
          loading={formLoading}
        />
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-8">
          <div>
            <h1 className="text-4xl font-bold text-gray-900 mb-2">
              Gestión de Productos
            </h1>
            <p className="text-gray-600">
              Administra el catálogo de productos
            </p>
          </div>
          <button 
            onClick={handleCreate} 
            className="mt-4 md:mt-0 btn-primary flex items-center space-x-2"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            <span>Nuevo Producto</span>
          </button>
        </div>

        {/* Alertas */}
        {error && (
          <div className="mb-6 alert alert-error">
            <div className="flex items-center">
              <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
              </svg>
              {error}
            </div>
          </div>
        )}

        {success && (
          <div className="mb-6 alert alert-success">
            <div className="flex items-center">
              <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
              </svg>
              {success}
            </div>
          </div>
        )}

        {/* Estadísticas */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="card text-center">
            <h3 className="text-sm font-medium text-gray-500 uppercase tracking-wide mb-2">
              Total Productos
            </h3>
            <p className="text-3xl font-bold text-blue-600">{products.length}</p>
          </div>
          <div className="card text-center">
            <h3 className="text-sm font-medium text-gray-500 uppercase tracking-wide mb-2">
              Con Stock
            </h3>
            <p className="text-3xl font-bold text-green-600">
              {products.filter(p => p.stock > 0).length}
            </p>
          </div>
          <div className="card text-center">
            <h3 className="text-sm font-medium text-gray-500 uppercase tracking-wide mb-2">
              Sin Stock
            </h3>
            <p className="text-3xl font-bold text-red-600">
              {products.filter(p => p.stock === 0).length}
            </p>
          </div>
        </div>

        {/* Tabla de productos */}
        <ProductTable
          products={products}
          onEdit={handleEdit}
          onDelete={handleDelete}
          loading={loading}
        />
      </div>
    </div>
  )
}

export default AdminProducts
