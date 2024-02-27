import { paths } from 'src/routes/paths';

// ----------------------------------------------------------------------

// export const pageLinks = [
//   {
//     isNew: true,
//     order: '6',
//     subheader: 'E-commerce-dev',
//     cover: '/assets/images/menu/menu_ecommerce.jpg',
//     items: [
//       { title: 'Product', path: paths.eCommerceDev.product },
//       { title: 'input', path: paths.inputForm },
//     ],
//   },
// ];

export const navConfig = [
  { title: '메뉴확인', path: '/' },
  // { title: 'Components', path: paths.components.root },
  // { title: 'Product', path: paths.eCommerceDev.product },
  { title: '입력폼', path: paths.inputForm },
  // {
  //   title: 'Pages',
  //   path: paths.pages,
  //   children: [pageLinks[0]],
  // },
  // { title: 'Docs', path: paths.docs },
];
