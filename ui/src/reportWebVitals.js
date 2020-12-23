import ReactGA from 'react-ga';

export const sendToGoogleAnalytics = ({name, delta, id}) => {
    ReactGA.ga('send', 'event', {
        eventCategory: 'Web Vitals',
        eventAction: name,
        // Google Analytics metrics must be integers, so the value is rounded.
        // For CLS the value is first multiplied by 1000 for greater precision
        // (note: increase the multiplier for greater precision if needed).
        eventValue: Math.round(name === 'CLS' ? delta * 1000 : delta),
        // The `id` value will be unique to the current page load. When sending
        // multiple values from the same page (e.g. for CLS), Google Analytics can
        // compute a total by grouping on this ID (note: requires `eventLabel` to
        // be a dimension in your report).
        eventLabel: id,
        // Use a non-interaction event to avoid affecting bounce rate.
        nonInteraction: true,
        // Use `sendBeacon()` if the browser supports it.
        transport: 'beacon',
    });
};


const reportWebVitals = onPerfEntry => {
    if (onPerfEntry && onPerfEntry instanceof Function) {
        import('web-vitals').then(({getCLS, getFID, getFCP, getLCP, getTTFB}) => {
            getCLS(onPerfEntry);
            getFID(onPerfEntry);
            getFCP(onPerfEntry);
            getLCP(onPerfEntry);
            getTTFB(onPerfEntry);
        });
    }
};

export default reportWebVitals;

