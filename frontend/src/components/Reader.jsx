import React, { useEffect, useRef } from "react";
import { BrowserQRCodeReader } from "@zxing/browser";
import { postInvoices } from "../service/invoices";

function Reader({ setQrCode, setActiveCam, setData }) {
    const videoRef = useRef(null);
    const processingRef = useRef(false);

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
                    async (result) => {
                        if (!result || processingRef.current) {
                            return;
                        }

                        processingRef.current = true;

                        try {
                            const url = result.getText();

                            setQrCode(url);

                            const products = await postInvoices(url);

                            if (products) {
                                setData(products);
                            }
                        } finally {
                            processingRef.current = false;
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
            controls?.stop();
            reader.reset();
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
