import { CanActivateFn, Router } from '@angular/router';

export const roleGuard: CanActivateFn = (route, state) => {

  let router: Router = new Router();

  const expectedRoles: string[] = route.data['roles'];
  const userRole = (localStorage.getItem("role") || "");

  if(expectedRoles.includes(userRole))
    return true;
    
  router.navigateByUrl("/access-denied");
  return false;
};
