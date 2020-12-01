import { API_ROOT, OCE } from '../state/oce-state';

/**
 * This is just for testing the OCE State API
 *
 import Makueni from '../oce/makueni';
 const makueni = new Makueni();
 makueni.init();
 */
class Makueni {
  init() {
    console.log('------------------------------------------------------------');
    const MAKUENI = OCE.substate({ name: 'makueni' });
    
    const input1 = MAKUENI.input({
      name: 'Input 1',
      initial: `${API_ROOT}/procuringEntitiesAwardsCount`,
    });
    
    const input2 = MAKUENI.input({
      name: 'Input 2',
      initial: 'aaa',
    });
    
    const filters1 = MAKUENI.mapping({
      name: 'Filters 1',
      deps: [input1],
      mapper: (input1) => {
        console.log('--- [Filter 1] ' + input1);
        // return input1;
        return { a: 1, b: 2 };
      }
    });
    
    const filters2 = MAKUENI.mapping({
      name: 'Filters 2',
      deps: [input2, filters1],
      mapper: (input2, filters1) => {
        console.log('=== [Filter 2] ' + input2);
        return input2;
      }
    });
    
    const remote1 = MAKUENI.remote({
      name: 'Remote1',
      url: input1,
      params: filters1,
      mapper: (test) => console.log(test)
    });
    
    const remote1Results = MAKUENI.mapping({
      name: 'Remote1Results',
      deps: [remote1],
      mapper: (test) => console.log(test)
    });
    
    /*
    filters2.getState('[[Filter 2]]');
    input1.assign('[[Input 1]]', 'bbbb');
    
    setTimeout(function () {
      input1.assign('[[Input 1]]', 'cccc');
  
      setTimeout(function () {
        filters2.getState('[[Filter 2]]');
      }, 100);
      
    }, 100);*/
    
    
    remote1Results.getState('[[Remote 1]]');
  }
}

export default Makueni;
