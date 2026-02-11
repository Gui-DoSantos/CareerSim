import styles from '../Styles/LinksNavigate.module.css'
import ButtonLinks from './buttonLinks';

function LinksNavigate() {
    return (
      <div className={styles.containerNavigate}>
        <ButtonLinks text="Ir para Menu" to="/Play" />
        <ButtonLinks text="Ir para Menu" to="/Estastisticas" />
        <ButtonLinks text="Ir para Menu" to="/News" />
        <p>ESSA PAGINA È DE MENU (vai ficar os quadrados)</p>
      </div>
    ); 
}

export default LinksNavigate