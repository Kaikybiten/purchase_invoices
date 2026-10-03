import React, { useEffect, useRef, useState } from "react";
import { BrowserMultiFormatReader } from "@zxing/browser";

function Reader(setQrCode) {
    const videoRef = useRef(null);

    useEffect(() => {
        const reader = new BrowserMultiFormatReader();

        let controls;

        async function startScanner() {
            try {
                controls = await reader.decodeFromVideoDevice(
                    undefined,
                    videoRef.current,
                    (result) => {
                        if (result) {
                            setQrCode(result.getText());
                        }
                    },
                );
            } catch (error) {
                console.error("Erro ao acessar a câmera:", error);
            }
        }

        startScanner();

        return () => {
            controls?.stop();
            reader.reset();
        };
    }, []);

    return (
        <video
            ref={videoRef}
            style={{ width: "100%", borderRadius: 8 }}
            muted
            playsInline
        />
    );
}

export default Reader;
