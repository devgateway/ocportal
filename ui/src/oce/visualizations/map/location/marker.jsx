import React, { useEffect } from 'react';
import { Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';

const Location = ({ position, data }) => {
  const map = useMap();

  useEffect(() => {
    const marker = L.marker(position).addTo(map);

    const onPopupOpen = (e) => {
      const px = map.project(e.target.getLatLng()); // find the pixel location on the map where the popup anchor is
      px.y -= e.target._container.clientHeight / 2; // find the height of the popup container, divide by 2, subtract from the Y axis of marker location
      map.panTo(map.unproject(px), { animate: true }); // pan to new center
    };

    marker.on('popupopen', onPopupOpen);

    return () => {
      marker.off('popupopen', onPopupOpen);
      marker.remove(); // Clean up the marker on unmount
    };
  }, [map, position]);

  return (
    <Marker position={position} data={data}>
      <Popup>{data.content}</Popup>
    </Marker>
  );
};

export default Location;
