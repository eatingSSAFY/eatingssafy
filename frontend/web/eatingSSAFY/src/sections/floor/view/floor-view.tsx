'use client';

import { useEffect, useState } from 'react';
import FloorHero from '../floor-hero';
import { getLunchTime } from 'src/api/request';
import Loading from 'src/app/loading';
import { LunchTimeData } from '../type';

// ----------------------------------------------------------------------'

export default function FloorView() {

  const [floorData, setFloorData] = useState<LunchTimeData>([])
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {

    getLunchTime().then(response => {
      setFloorData(response);
      setIsLoading(false);
    });

  }, [])

  return (
    <>
      {!isLoading ? <FloorHero floors={floorData}/> : <Loading />}
    </>
  );
}