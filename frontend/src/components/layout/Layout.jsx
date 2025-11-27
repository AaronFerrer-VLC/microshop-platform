import Navbar from './Navbar'
import Footer from './Footer'

/**
 * Componente de layout principal que envuelve todas las páginas.
 * 
 * @param {Object} props - Propiedades del componente
 * @param {React.ReactNode} props.children - Contenido a renderizar
 * @returns {JSX.Element} Layout con navbar y footer
 */
function Layout({ children }) {
  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
      <main className="flex-1">
        {children}
      </main>
      <Footer />
    </div>
  )
}

export default Layout
