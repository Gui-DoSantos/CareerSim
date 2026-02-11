import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import StartScreen from "./Pages/StartScreen"
import Menu from "./Pages/Menu"
import Play from "./Pages/Play";

export const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* { <Route path="/" element={<Home />} />  } EXEMPLO: */}
        <Route path="/" element={<StartScreen />} />

        <Route path="/StartScreen" element={<StartScreen />} />

        <Route path="/Menu" element={<Menu />} />

        <Route path="/Play" element={<Play />} />


        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
};
