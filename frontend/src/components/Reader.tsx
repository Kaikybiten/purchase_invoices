import React, { Dispatch, SetStateAction, useState } from "react";
import { useQrScanner } from "./reader/useQrScanner";
import { processInvoice } from "./reader/processInvoice";
import { RecordedInvoice } from "../types/invoice";

type ReaderProps = {
    setQrCode: Dispatch<string>;
    setActiveCam: Dispatch<boolean>;
    setData: Dispatch<RecordedInvoice>;
};

function Reader({ setQrCode, setActiveCam, setData }: ReaderProps) {
    const [loading, setLoading] = useState(false);

    // Função
    const videoRef = useQrScanner(async (url: string) => {
        setLoading(true);

        try {
            await processInvoice(url, setQrCode, setData);
        } finally {
            setLoading(false);
        }
    }, setActiveCam);

    return (
        <div>
            <div className="overflow-hidden rounded-xl bg-gray-500">
                <video
                    ref={videoRef}
                    className="aspect-video w-full object-cover"
                    muted
                    playsInline
                />
            </div>

            {loading && (
                <p className="mt-3 text-sm text-black-600">
                    Consultando nota fiscal...
                </p>
            )}
        </div>
    );
}

export default Reader;
