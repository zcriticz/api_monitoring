import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { ConfigProvider } from "antd";
import "./index.css";
import ptBR from "antd/locale/pt_BR";
import App from "./App.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ConfigProvider
      locale={ptBR}
      theme={{
        token: {
          colorPrimary: "#a8055a",
        },
      }}
    >
      <App />
    </ConfigProvider>
  </StrictMode>,
);
