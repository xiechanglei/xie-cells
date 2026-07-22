import { NotFoundView } from '@/sections/error';

// ----------------------------------------------------------------------

export default function Page() {
  return (
    <>
      <title>{`404 page not found! | Error`}</title>

      <NotFoundView />
    </>
  );
}
