import MainRoutes from './section-main';
import AuthRoutes from './section-auth';
import ErrorRoutes from './section-error';
import type {RouteObject} from 'react-router';

// export all routes
export const routesSection: RouteObject[] = [...MainRoutes, ...AuthRoutes, ...ErrorRoutes]