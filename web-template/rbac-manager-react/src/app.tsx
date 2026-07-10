import {createRoot} from 'react-dom/client'
import {router, RouterProvider} from "./router";
import {Provider} from "@/components/theme/provider"

/**
 * this is the entry point of the application, it will render the root component into the DOM
 */

createRoot(document.getElementById('root')!).render(<Provider>
    <RouterProvider router={router}/>
</Provider>)