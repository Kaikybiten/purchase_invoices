export async function postInvoices() {
    const response = await fetch("/invoices", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            url: "https://www.nfce.fazenda.sp.gov.br/NFCeConsultaPublica/Paginas/ConsultaQRCode.aspx?p=35260971779813001818651080001114361002221145|2|1|1|8ee03d5ac0b2f25cd40295ee7f3133957694d578",
        }),
    });

    if (!response.ok) {
        console.log("Não foi possível ler nota");
        return;
    }

    const responseData = await response.json();

    const data = responseData.data;

    return data.products;
}
