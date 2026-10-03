import React, { useState } from "react";
import { createRoot } from "react-dom/client";
import Reader from "./components/Reader";
import Invoice from "./components/invoice/Invoice";

import { postInvoices } from "./service/invoices";

function App() {
    const [qrCode, setQrCode] = useState("");
    const [data, setData] = useState([]);

    const handleInvoice = async () => {
        const invoices = await postInvoices();
        setData(invoices);
    };

    return (
        <main
            style={{
                maxWidth: 500,
                margin: "40px auto",
                padding: 20,
                fontFamily: "sans-serif",
            }}
        >
            <h1>Leitor de QR Code</h1>

            <Reader setQrCode={setQrCode} />

            <p>
                <strong>Valor lido:</strong>
            </p>

            <input
                value={qrCode}
                readOnly
                placeholder="Aponte a câmera para um QR Code"
                style={{
                    width: "100%",
                    padding: 10,
                    boxSizing: "border-box",
                }}
            />

            <button onClick={handleInvoice}>clica aqui</button>

            <Invoice data={data} />
        </main>
    );
}

createRoot(document.getElementById("root")).render(<App />);
