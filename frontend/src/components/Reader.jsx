import React, { useEffect, useRef } from "react";
import { BrowserQRCodeReader } from "@zxing/browser";
import { postInvoices } from "../service/invoices";

function Reader({ setQrCode, setActiveCam, setData }) {
    const videoRef = useRef(null);

    useEffect(() => {
        let controls;

        // Cria leitor da ZXing
        const reader = new BrowserQRCodeReader();

        async function startScanner() {
            try {
                // Inicia a camera armazenando em uma variavel para controle
                controls = await reader.decodeFromVideoDevice(
                    undefined, // deviceId da câmera; undefined = deixa a biblioteca escolher

                    videoRef.current, // Elemento de video que receberá a câmera

                    // Callback chamado quando um QR Code é detectado - 'result' contém o resultado da leitura
                    (result) => {
                        if (result) {
                            const url = result.getText();
                            const products = await postInvoices(url)

                            setData(products)

                        }
                    },
                );

                setActiveCam(true);
            } catch (error) {
                console.error("Erro ao acessar a câmera:", error);
                setActiveCam(false);
            }
        }

        startScanner();

        // Quando o componente deixar de existir:
        return () => {
            controls?.stop(); // Para câmera
            reader.reset(); // Reinicia leitor

            setActiveCam(false);
        };
    }, []);

    return (
        <div className="overflow-hidden rounded-xl bg-gray-500">
            <video
                ref={videoRef}
                className="aspect-video w-full object-cover"
                muted
                playsInline
            />
        </div>
    );
}

export default Reader;
