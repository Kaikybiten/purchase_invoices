import { Dispatch, useEffect, useRef } from "react";
import { BrowserQRCodeReader, IScannerControls } from "@zxing/browser";

// Criando leitor do QrCode
export function useQrScanner(
    onScan: Dispatch<string>,
    setActiveCam: Dispatch<boolean>,
) {
    const videoRef = useRef<HTMLVideoElement>(null);
    const controlsRef = useRef<IScannerControls>(null);
    const processingRef = useRef(false);

    useEffect(() => {
        let mounted = true;
        const reader = new BrowserQRCodeReader();

        async function startScanner() {
            if (!videoRef.current) return;

            try {
                const controls = await reader.decodeFromVideoDevice(
                    undefined,
                    videoRef.current,
                    async (result) => {
                        if (!result || processingRef.current || !mounted) {
                            return;
                        }

                        processingRef.current = true;

                        controls.stop();
                        controlsRef.current = null;
                        setActiveCam(false);

                        try {
                            await onScan(result.getText());
                        } catch (error) {
                            console.error("Erro ao processar a nota:", error);
                        } finally {
                            processingRef.current = false;
                        }
                    },
                );

                if (!mounted) {
                    controls.stop();
                    return;
                }

                controlsRef.current = controls;

                setActiveCam(true);
            } catch (error) {
                console.error("Erro ao acessar a câmera:", error);

                if (mounted) {
                    setActiveCam(false);
                }
            }
        }

        startScanner();

        return () => {
            mounted = false;
            controlsRef.current?.stop();
            controlsRef.current = null;
            setActiveCam(false);
        };
    }, []);

    return videoRef;
}
