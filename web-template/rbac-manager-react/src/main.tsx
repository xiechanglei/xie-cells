import {createRoot} from 'react-dom/client'
import {router, RouterProvider} from "./router";

const root = createRoot(document.getElementById('root')!)

root.render(<RouterProvider router={router}/>)