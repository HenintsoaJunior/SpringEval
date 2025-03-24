document.addEventListener('DOMContentLoaded', function() {
    // Get data from the window object that was populated in the inline script
    const chartData = window.dashboardData.chart1Data;
    const chart2DataJson = window.dashboardData.chart2DataJson;
    const chart3DataJson = window.dashboardData.chart3DataJson;
    const selectedYear = window.dashboardData.selectedYear;
    const selectedMonth = window.dashboardData.selectedMonth;
    
    // Chart 1: Statistiques des Clients
    if (chartData) {
        // Préparation des données pour le graphique
        const labels = chartData.map(data => data.company_name);
        const totalInvoices = chartData.map(data => data.total_invoices);
        const totalInvoiced = chartData.map(data => data.total_invoiced_amount || 0);
        const outstanding = chartData.map(data => data.outstanding_amount || 0);
        const externalIds = chartData.map(data => data.external_id);
        
        const ctx = document.getElementById('mainChart').getContext('2d');
        const mainChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Nombre de factures',
                    data: totalInvoices,
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }, {
                    label: 'Montant facturé',
                    data: totalInvoiced,
                    backgroundColor: 'rgba(75, 192, 192, 0.5)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }, {
                    label: 'Montant restant',
                    data: outstanding,
                    backgroundColor: 'rgba(255, 99, 132, 0.5)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Statistiques des Clients'
                    }
                },
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const externalId = externalIds[index];
                        window.location.href = `/api/spring/client/${externalId}`;
                    }
                }
            }
        });

        const ctxInvoices = document.getElementById('invoiceChart').getContext('2d');
        const invoiceChart = new Chart(ctxInvoices, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Nombre de factures',
                    data: totalInvoices,
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: true,
                scales: {
                    x: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Nombre de factures'
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                    }
                },
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const externalId = externalIds[index];
                        window.location.href = `/api/spring/client/${externalId}`;
                    }
                }
            }
        });
    }
    
    // Chart 2: Statistiques des Paiements
    let paymentChartData;
    
    try {
        paymentChartData = JSON.parse(chart2DataJson);
    } catch (e) {
        paymentChartData = [];
        console.error('Error parsing chart2DataJson:', e);
    }

    if (paymentChartData && Array.isArray(paymentChartData) && paymentChartData.length > 0) {
        const monthNames = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 
                          'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc'];
        
        // Create unique labels for each payment
        const labels = paymentChartData.map(item => {
            const [year, month] = item.payment_month.split('-');
            const monthName = monthNames[parseInt(month) - 1];
            const shortId = item.payment_external_id.substring(0, 8);
            return `${monthName} ${year} (${shortId}...)`;
        });

        const paidAmounts = paymentChartData.map(item => parseFloat(item.total_paid_amount) || 0);
        const invoicedAmounts = paymentChartData.map(item => parseFloat(item.total_invoiced_amount) || 0);
        const outstandingAmounts = paymentChartData.map(item => parseFloat(item.outstanding_amount) || 0);
        const paymentIds = paymentChartData.map(item => item.payment_external_id);

        const ctx = document.getElementById('paymentChart').getContext('2d');
        const chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Montant payé',
                    data: paidAmounts,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1,
                    paymentIds: paymentIds
                }, {
                    label: 'Montant facturé',
                    data: invoicedAmounts,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    paymentIds: paymentIds
                }, {
                    label: 'Montant restant',
                    data: outstandingAmounts,
                    backgroundColor: 'rgba(255, 99, 132, 0.6)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1,
                    paymentIds: paymentIds
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: { display: true, text: 'Montant' }
                    },
                    x: {
                        title: { display: true, text: 'Paiements par mois' },
                        ticks: {
                            autoSkip: false,
                            maxRotation: 45,
                            minRotation: 45
                        }
                    }
                },
                plugins: {
                    legend: { position: 'top' },
                    title: {
                        display: true,
                        text: `Statistiques des paiements pour ${selectedYear}` + 
                                (selectedMonth ? ` - ${monthNames[parseInt(selectedMonth) - 1]}` : '')
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.dataset.label || '';
                                const value = context.parsed.y || 0;
                                const paymentId = context.dataset.paymentIds[context.dataIndex];
                                return `${label}: ${value.toLocaleString()} (ID: ${paymentId})`;
                            }
                        }
                    }
                },
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const element = elements[0];
                        const datasetIndex = element.datasetIndex;
                        const index = element.index;
                        const paymentId = chart.data.datasets[datasetIndex].paymentIds[index];
                        if (paymentId) {
                            window.location.href = `/api/spring/payment/${paymentId}`;
                        }
                    }
                }
            }
        });
    }
    
    // Chart 3: Statistiques des Factures
    let invoiceChartData;
    
    try {
        invoiceChartData = JSON.parse(chart3DataJson);
    } catch (e) {
        invoiceChartData = [];
        console.error('Error parsing chart3DataJson:', e);
    }

    if (invoiceChartData && Array.isArray(invoiceChartData) && invoiceChartData.length > 0) {
        // Prepare data for the chart
        const labels = invoiceChartData.map(item => item.invoice_external_id.substring(0, 8) + '...');
        const paidAmounts = invoiceChartData.map(item => parseFloat(item.total_paid_amount) || 0);
        const invoicedAmounts = invoiceChartData.map(item => parseFloat(item.total_invoiced_amount) || 0);
        const outstandingAmounts = invoiceChartData.map(item => parseFloat(item.outstanding_amount) || 0);

        const ctx = document.getElementById('invoiceChart3').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Montant payé',
                    data: paidAmounts,
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }, {
                    label: 'Montant facturé',
                    data: invoicedAmounts,
                    backgroundColor: 'rgba(75, 192, 192, 0.5)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }, {
                    label: 'Montant restant',
                    data: outstandingAmounts,
                    backgroundColor: 'rgba(255, 99, 132, 0.5)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Montant'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'ID Facture'
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top'
                    },
                    title: {
                        display: true,
                        text: 'Statistiques des factures par ID'
                    }
                }
            }
        });
    }
});