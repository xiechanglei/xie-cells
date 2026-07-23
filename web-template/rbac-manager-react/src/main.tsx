import {createRoot} from 'react-dom/client';
import {Outlet, RouterProvider, createHashRouter} from 'react-router';

import {App} from './app';
import {routesSection} from "@/router/sections";

const router = createHashRouter([
    {
        Component: () => (
            <App>
                <Outlet/>
            </App>
        ),
        children: routesSection,
    },
]);

const root = createRoot(document.getElementById('root')!);

root.render(<RouterProvider router={router}/>);