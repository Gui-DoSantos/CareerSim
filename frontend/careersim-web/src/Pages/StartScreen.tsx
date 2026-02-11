import { useNavigate } from 'react-router-dom'
import styles from '../Styles/StartScreen.module.css'


function StartScreen() {

    const navigate = useNavigate()

    const handleClick = () => {
        navigate('/menu')
    }
    
    return (
        <div className={styles.container}>
            <p>ESSA PAGINA DE INICIO, (VAI FICAR O WALLPAPER DO JOGO AQUI)</p>
                <button onClick={handleClick}>
                    IR PARA MENU
                </button>
        </div>
    )
}

export default StartScreen