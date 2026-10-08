import React, { useEffect, useRef } from "react";
import { BrowserQRCodeReader } from "@zxing/browser";
import { postInvoices } from "../service/invoices";

function Reader({ setQrCode, setActiveCam, setData }) {
    const videoRef = useRef(null);
    const readerRef = useRef(null);
    const controlsRef = useRef(null);
    const processingRef = useRef(false);

    useEffect(() => {
        const reader = new BrowserQRCodeReader();

        readerRef.current = reader;

        async function startScanner() {
            try {
                controlsRef.current = await reader.decodeFromVideoDevice(
                    undefined,
                    videoRef.current,
                    async (result) => {
                        if (!result || processingRef.current) {
                            return;
                        }

                        processingRef.current = true;

                        // Para a câmera assim que o QR Code for lido
                        controlsRef.current?.stop();
                        setActiveCam(false);

                        try {
                            const url = result.getText();

                            setQrCode(url);

                            const products = await postInvoices(url);

                            if (products) {
                                setData(products);
                            }
                        } catch (error) {
                            console.error("Erro ao processar a nota:", error);
                        } finally {
                            processingRef.current = false;

                            // Liga a câmera novamente
                            await startScanner();
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

        return () => {
            controlsRef.current?.stop();
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
