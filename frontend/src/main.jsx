import "./index.css";
import React, { useState } from "react";
import { createRoot } from "react-dom/client";
import Reader from "./components/Reader";
import Invoice from "./components/invoice/Invoice";

function App() {
    const [qrCode, setQrCode] = useState("");
    const [data, setData] = useState([]);
    const [activeCam, setActiveCam] = useState(false);

    return (
        <main className="min-h-screen bg-slate-100 px-4 py-10">
            <div className="mx-auto max-w-2xl">
                <header className="mb-6">
                    <span className="text-xs font-bold uppercase tracking-widest text-slate-500">
                        NFC-e
                    </span>

                    <h1 className="mt-1 text-3xl font-bold tracking-tight text-slate-900">
                        Leitor de Nota Fiscal
                    </h1>

                    <p className="mt-2 text-sm text-slate-500">
                        Aponte a câmera para o QR Code da nota fiscal.
                    </p>
                </header>

                <section className="border border-slate-200 bg-white p-5 shadow-sm">
                    <div className="mb-4 flex items-center justify-between">
                        <span className="flex items-center gap-2 text-xs font-medium text-slate-500">
                            <span
                                className={`h-2 w-2 rounded-full ${activeCam ? "bg-green-500" : "bg-red-500"}`}
                            />
                            Câmera {activeCam ? "ativa" : "desativada"}
                        </span>
                    </div>

                    <Reader
                        setQrCode={setQrCode}
                        setActiveCam={setActiveCam}
                        setData={setData}
                    />

                    <div className="mt-5">
                        <label className="mb-2 block text-sm font-medium text-slate-700">
                            QR Code lido
                        </label>

                        <input
                            value={qrCode}
                            readOnly
                            placeholder="O código aparecerá aqui..."
                            className="w-full rounded-lg border border-slate-300 bg-slate-50 px-3 py-3 text-sm text-slate-600 outline-none"
                        />
                    </div>
                </section>

                {data.length > 0 && (
                    <section className="text-black font-mono mt-5 bg-white p-5 shadow-sm">
                        <div className="mb-4 flex items-center justify-between">
                            <div>
                                <h2 className="mt-1 font-semibold text-3xl">
                                    PRODUTOS REGISTRADOS
                                </h2>
                            </div>
                        </div>

                        <Invoice data={data} />
                    </section>
                )}
            </div>
        </main>
    );
}

createRoot(document.getElementById("root")).render(<App />);
