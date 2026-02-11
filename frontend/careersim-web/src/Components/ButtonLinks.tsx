import styles from '../Styles/ButtonLinkComponent.module.css'
import { useNavigate } from "react-router-dom";


interface LinksTypes {
  text: string;
  to: string; 
}

function ButtonLinks({ text , to }: LinksTypes) {

  const navigate = useNavigate()

 

  return (
    <button className={styles.buttonContainer} onClick={() => navigate(to)}>
      {text}
    </button>
  );
}

export default ButtonLinks;
